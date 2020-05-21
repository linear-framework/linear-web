package com.linearframework.web.internal

import org.reflections.Reflections
import java.lang.reflect.Modifier
import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

private[web] object Utils {

  def findScalaObjects[T: ClassTag](inPackage: String)(implicit classTag: ClassTag[T]): Seq[T] = {
    val mirror = runtimeMirror(getClass.getClassLoader)
    new Reflections(inPackage)
      .getSubTypesOf(classTag.runtimeClass)
      .asScala
      .toSeq
      .filterNot(_.isInterface)
      .filterNot(subClass => Modifier.isAbstract(subClass.getModifiers))
      .map(_.getName.replace("$", ""))
      .map { objectName =>
        val module = mirror.staticModule(objectName)
        mirror.reflectModule(module).instance.asInstanceOf[T]
      }
  }

  def printableTable(columnHeadings: Seq[String], rows: Seq[Seq[String]]): String = {
    val indexedColumns = columnHeadings.zipWithIndex
    var longestValues = indexedColumns.map({ case (heading, colNum) => colNum -> heading.length }).toMap

    rows.foreach(row => {
      row.zipWithIndex.foreach({ case (cell, colNum) =>
        if (longestValues(colNum) < cell.length) {
          longestValues = longestValues + (colNum -> cell.length)
        }
      })
    })

    val line = "+" + longestValues.toSeq
      .sortWith(_._1 < _._1)
      .map(_._2)
      .map(length => "".padTo(length + 4, "-").mkString(""))
      .mkString("+") + "+"

    var table = ""

    table += line + "\n|  "
    indexedColumns.foreach({ case (heading, colNum) =>
      table += heading.padTo(longestValues(colNum), " ").mkString("") + "  |  "
    })
    table += "\n" + line + "\n"

    rows.foreach(row => {
      table += "|  "
      row.zipWithIndex.foreach({ case (cell, colNum) =>
        table += cell.padTo(longestValues(colNum), " ").mkString("") + "  |  "
      })
      table += "\n"
    })
    table += line + "\n"

    table
  }

}
