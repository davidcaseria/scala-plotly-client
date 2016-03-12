package co.theasi.plotly

case class DrawnGrid(
    fileId: String,
    fileName: String = "",
    columnUids: Map[String, String] = Map.empty)
