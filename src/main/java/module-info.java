module com.epp.epp_reader_compressor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.epp.epp_reader_compressor to javafx.fxml;
    exports com.epp.epp_reader_compressor;
}