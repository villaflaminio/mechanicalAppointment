package it.mtempobono.mechanicalappointment.controller;

import it.mtempobono.mechanicalappointment.model.Place;
import it.mtempobono.mechanicalappointment.repository.PlaceRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("api/public")
public class PublicController {

    @Autowired
    private PlaceRepository placeRepository;

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(";");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    //popolate db
    @GetMapping("/populate")
    private void populate() throws IOException {


        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/listacomuni.txt", StandardCharsets.ISO_8859_1))) {
            String line = br.readLine();
            while (line != null) {
                records.add(getRecordFromLine(line));
                line = br.readLine();
            }
        }

        for (List<String> record : records) {
            String Istat = record.get(0);
            String Comune = record.get(1).replace("'", "\\'");
            String Provincia = record.get(2);
            String Regione = record.get(3);

            //create and popolate place with builder
            Place place = Place.builder()
                    .istat(Long.parseLong(Istat))
                    .municipality(Comune)
                    .province(Provincia)
                    .region(Regione)
                    .build();
            System.out.println("add place: " + place);
            placeRepository.save(place);
        }

    }

}
