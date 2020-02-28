package com.linearframework.web

/**
 * Represents a standard MIME type.
 * All text-based MIME types are assumed to have UTF-8 encoding; other encodings are not supported.
 */
sealed abstract class ContentType(`type`: String, subType: String, charset: Option[String] = None) {
  override final def toString: String = {
    val charsetString = charset match {
      case Some(c) => s"; charset=$c"
      case None => ""
    }
    s"${`type`}/$subType$charsetString"
  }
}

case object AAC_AUDIO extends ContentType("audio", "aac", None)
case object APPLE_MOBILE_CONFIG extends ContentType("application", "x-apple-aspen-config", None)
case object APPLE_PASSBOOK extends ContentType("application", "vnd.apple.pkpass", None)
case object APPLICATION_BINARY extends ContentType("application", "binary", None)
case object APPLICATION_XML extends ContentType("application", "xml", Some("utf-8"))
case object ATOM extends ContentType("application", "atom+xml", Some("utf-8"))
case object BASIC_AUDIO extends ContentType("audio", "basic", None)
case object BMP extends ContentType("image", "bmp", None)
case object BZIP2 extends ContentType("application", "x-bzip2", None)
case object CACHE_MANIFEST extends ContentType("text", "cache-manifest", Some("utf-8"))
case object CRW extends ContentType("image", "x-canon-crw", None)
case object CSS extends ContentType("text", "css", Some("utf-8"))
case object CSV extends ContentType("text", "csv", Some("utf-8"))
case object DART extends ContentType("application", "dart", Some("utf-8"))
case object EOT extends ContentType("application", "vnd.ms-fontobject", None)
case object EPUB extends ContentType("application", "epub+zip", None)
case object FLV_VIDEO extends ContentType("video", "x-flv", None)
case object FORM_DATA extends ContentType("application", "x-www-form-urlencoded", None)
case object GEO_JSON extends ContentType("application", "geo+json", None)
case object GIF extends ContentType("image", "gif", None)
case object GZIP extends ContentType("application", "x-gzip", None)
case object HAL_JSON extends ContentType("application", "hal+json", None)
case object HTML extends ContentType("text", "html", Some("utf-8"))
case object I_CALENDAR extends ContentType("text", "calendar", Some("utf-8"))
case object ICO extends ContentType("image", "vnd.microsoft.icon", None)
case object JAVASCRIPT extends ContentType("application", "javascript", Some("utf-8"))
case object JOSE extends ContentType("application", "jose", None)
case object JOSE_JSON extends ContentType("application", "jose+json", None)
case object JPEG extends ContentType("image", "jpeg", None)
case object JSON extends ContentType("application", "json", Some("utf-8"))
case object KEY_ARCHIVE extends ContentType("application", "pkcs12", None)
case object KML extends ContentType("application", "vnd.google-earth.kml+xml", None)
case object KMZ extends ContentType("application", "vnd.google-earth.kmz", None)
case object L16_AUDIO extends ContentType("audio", "l16", None)
case object L24_AUDIO extends ContentType("audio", "l24", None)
case object MANIFEST_JSON extends ContentType("application", "manifest+json", Some("utf-8"))
case object MBOX extends ContentType("application", "mbox", None)
case object MICROSOFT_EXCEL extends ContentType("application", "vnd.ms-excel", None)
case object MICROSOFT_OUTLOOK extends ContentType("application", "vnd.ms-outlook", None)
case object MICROSOFT_POWERPOINT extends ContentType("application", "vnd.ms-powerpoint", None)
case object MICROSOFT_WORD extends ContentType("application", "msword", None)
case object MP4_AUDIO extends ContentType("audio", "mp4", None)
case object MP4_VIDEO extends ContentType("video", "mp4", None)
case object MPEG_AUDIO extends ContentType("audio", "mpeg", None)
case object MPEG_VIDEO extends ContentType("video", "mpeg", None)
case object NACL_APPLICATION extends ContentType("application", "x-nacl", None)
case object NACL_PORTABLE_APPLICATION extends ContentType("application", "x-pnacl", None)
case object OCTET_STREAM extends ContentType("application", "octet-stream", None)
case object OGG_AUDIO extends ContentType("audio", "ogg", None)
case object OGG_CONTAINER extends ContentType("application", "ogg", None)
case object OGG_VIDEO extends ContentType("video", "ogg", None)
case object OOXML_DOCUMENT extends ContentType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document", None)
case object OOXML_PRESENTATION extends ContentType("application", "vnd.openxmlformats-officedocument.presentationml.presentation", None)
case object OOXML_SHEET extends ContentType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet", None)
case object OPENDOCUMENT_GRAPHICS extends ContentType("application", "vnd.oasis.opendocument.graphics", None)
case object OPENDOCUMENT_PRESENTATION extends ContentType("application", "vnd.oasis.opendocument.presentation", None)
case object OPENDOCUMENT_SPREADSHEET extends ContentType("application", "vnd.oasis.opendocument.spreadsheet", None)
case object OPENDOCUMENT_TEXT extends ContentType("application", "vnd.oasis.opendocument.text", None)
case object PDF extends ContentType("application", "pdf", None)
case object PLAIN_TEXT extends ContentType("text", "plain", Some("utf-8"))
case object PNG extends ContentType("image", "png", None)
case object POSTSCRIPT extends ContentType("application", "postscript", None)
case object PROTOBUF extends ContentType("application", "protobuf", None)
case object PSD extends ContentType("image", "vnd.adobe.photoshop", None)
case object QUICKTIME extends ContentType("video", "quicktime", None)
case object RDF_XML extends ContentType("application", "rdf+xml", Some("utf-8"))
case object RTF extends ContentType("application", "rtf", Some("utf-8"))
case object SFNT extends ContentType("application", "font-sfnt", None)
case object SHOCKWAVE_FLASH extends ContentType("application", "x-shockwave-flash", None)
case object SKETCHUP extends ContentType("application", "vnd.sketchup.skp", None)
case object SOAP_XML extends ContentType("application", "soap+xml", Some("utf-8"))
case object SVG extends ContentType("image", "svg+xml", Some("utf-8"))
case object TAR extends ContentType("application", "x-tar", None)
case object TEXT_JAVASCRIPT extends ContentType("text", "javascript", Some("utf-8"))
case object THREE_GPP2_VIDEO extends ContentType("video", "3gpp2", None)
case object THREE_GPP_VIDEO extends ContentType("video", "3gpp", None)
case object TIFF extends ContentType("image", "tiff", None)
case object TSV extends ContentType("text", "tab-separated-values", Some("utf-8"))
case object VCARD extends ContentType("text", "vcard", Some("utf-8"))
case object VND_REAL_AUDIO extends ContentType("audio", "vnd.rn-realaudio", None)
case object VND_WAVE_AUDIO extends ContentType("audio", "vnd.wave", None)
case object VORBIS_AUDIO extends ContentType("audio", "vorbis", None)
case object VTT extends ContentType("text", "vtt", Some("utf-8"))
case object WASM_APPLICATION extends ContentType("application", "wasm", None)
case object WAX_AUDIO extends ContentType("audio", "x-ms-wax", None)
case object WEBM_AUDIO extends ContentType("audio", "webm", None)
case object WEBM_VIDEO extends ContentType("video", "webm", None)
case object WEBP extends ContentType("image", "webp", None)
case object WMA_AUDIO extends ContentType("audio", "x-ms-wma", None)
case object WML extends ContentType("text", "vnd.wap.wml", Some("utf-8"))
case object WMV extends ContentType("video", "x-ms-wmv", None)
case object WOFF extends ContentType("application", "font-woff", None)
case object WOFF2 extends ContentType("application", "font-woff2", None)
case object XHTML extends ContentType("application", "xhtml+xml", Some("utf-8"))
case object XML extends ContentType("text", "xml", Some("utf-8"))
case object XRD extends ContentType("application", "xrd+xml", Some("utf-8"))
case object ZIP extends ContentType("application", "zip", None)

case object WildcardContentType extends ContentType("*", "*", None)
case class CustomContentType(`type`: String, subType: String) extends ContentType(`type`, subType)

object ContentType {
  def apply(contentType: String): ContentType = {
    contentType.toLowerCase.replaceAll(";(.*)", "").trim match {
      case "application/atom+xml" => ATOM
      case "application/binary" => APPLICATION_BINARY
      case "application/dart" => DART
      case "application/epub+zip" => EPUB
      case "application/font-sfnt" => SFNT
      case "application/font-woff" => WOFF
      case "application/font-woff2" => WOFF2
      case "application/geo+json" => GEO_JSON
      case "application/hal+json" => HAL_JSON
      case "application/javascript" => JAVASCRIPT
      case "application/jose" => JOSE
      case "application/jose+json" => JOSE_JSON
      case "application/json" => JSON
      case "application/manifest+json" => MANIFEST_JSON
      case "application/mbox" => MBOX
      case "application/msword" => MICROSOFT_WORD
      case "application/octet-stream" => OCTET_STREAM
      case "application/ogg" => OGG_CONTAINER
      case "application/pdf" => PDF
      case "application/pkcs12" => KEY_ARCHIVE
      case "application/postscript" => POSTSCRIPT
      case "application/protobuf" => PROTOBUF
      case "application/rdf+xml" => RDF_XML
      case "application/rtf" => RTF
      case "application/soap+xml" => SOAP_XML
      case "application/vnd.apple.pkpass" => APPLE_PASSBOOK
      case "application/vnd.google-earth.kml+xml" => KML
      case "application/vnd.google-earth.kmz" => KMZ
      case "application/vnd.ms-excel" => MICROSOFT_EXCEL
      case "application/vnd.ms-fontobject" => EOT
      case "application/vnd.ms-outlook" => MICROSOFT_OUTLOOK
      case "application/vnd.ms-powerpoint" => MICROSOFT_POWERPOINT
      case "application/vnd.oasis.opendocument.graphics" => OPENDOCUMENT_GRAPHICS
      case "application/vnd.oasis.opendocument.presentation" => OPENDOCUMENT_PRESENTATION
      case "application/vnd.oasis.opendocument.spreadsheet" => OPENDOCUMENT_SPREADSHEET
      case "application/vnd.oasis.opendocument.text" => OPENDOCUMENT_TEXT
      case "application/vnd.openxmlformats-officedocument.presentationml.presentation" => OOXML_PRESENTATION
      case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" => OOXML_SHEET
      case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" => OOXML_DOCUMENT
      case "application/vnd.sketchup.skp" => SKETCHUP
      case "application/wasm" => WASM_APPLICATION
      case "application/x-apple-aspen-config" => APPLE_MOBILE_CONFIG
      case "application/x-bzip2" => BZIP2
      case "application/x-gzip" => GZIP
      case "application/x-nacl" => NACL_APPLICATION
      case "application/x-pnacl" => NACL_PORTABLE_APPLICATION
      case "application/x-shockwave-flash" => SHOCKWAVE_FLASH
      case "application/x-tar" => TAR
      case "application/x-www-form-urlencoded" => FORM_DATA
      case "application/xhtml+xml" => XHTML
      case "application/xml" => APPLICATION_XML
      case "application/xrd+xml" => XRD
      case "application/zip" => ZIP
      case "audio/aac" => AAC_AUDIO
      case "audio/basic" => BASIC_AUDIO
      case "audio/l16" => L16_AUDIO
      case "audio/l24" => L24_AUDIO
      case "audio/mp4" => MP4_AUDIO
      case "audio/mpeg" => MPEG_AUDIO
      case "audio/ogg" => OGG_AUDIO
      case "audio/vnd.rn-realaudio" => VND_REAL_AUDIO
      case "audio/vnd.wave" => VND_WAVE_AUDIO
      case "audio/vorbis" => VORBIS_AUDIO
      case "audio/webm" => WEBM_AUDIO
      case "audio/x-ms-wax" => WAX_AUDIO
      case "audio/x-ms-wma" => WMA_AUDIO
      case "image/bmp" => BMP
      case "image/gif" => GIF
      case "image/jpeg" => JPEG
      case "image/png" => PNG
      case "image/svg+xml" => SVG
      case "image/tiff" => TIFF
      case "image/vnd.adobe.photoshop" => PSD
      case "image/vnd.microsoft.icon" => ICO
      case "image/webp" => WEBP
      case "image/x-canon-crw" => CRW
      case "text/cache-manifest" => CACHE_MANIFEST
      case "text/calendar" => I_CALENDAR
      case "text/css" => CSS
      case "text/csv" => CSV
      case "text/html" => HTML
      case "text/javascript" => TEXT_JAVASCRIPT
      case "text/plain" => PLAIN_TEXT
      case "text/tab-separated-values" => TSV
      case "text/vcard" => VCARD
      case "text/vnd.wap.wml" => WML
      case "text/vtt" => VTT
      case "text/xml" => XML
      case "video/3gpp" => THREE_GPP_VIDEO
      case "video/3gpp2" => THREE_GPP2_VIDEO
      case "video/mp4" => MP4_VIDEO
      case "video/mpeg" => MPEG_VIDEO
      case "video/ogg" => OGG_VIDEO
      case "video/quicktime" => QUICKTIME
      case "video/webm" => WEBM_VIDEO
      case "video/x-flv" => FLV_VIDEO
      case "video/x-ms-wmv" => WMV
      case "*/*" => WildcardContentType
      case t if t.contains("/") => CustomContentType(t.substring(0, t.indexOf('/')), t.substring(t.indexOf('/') + 1))
      case x => throw new IllegalArgumentException(s"Content Type [$x] is not supported")
    }
  }
}

