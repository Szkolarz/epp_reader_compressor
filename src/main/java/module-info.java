module com.epp.epp_reader_compressor {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.logging;


    opens com.epp.epp_reader_compressor to javafx.fxml;
    exports com.epp.epp_reader_compressor;
}