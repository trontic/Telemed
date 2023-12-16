package com.telemed.model;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class RecordRepositoryMemory {

    List<Record> recordList = new ArrayList<>();

    public RecordRepositoryMemory() {
        Record r = new Record(122, 81, 85, 36, "10.12.2023.", "07:44");
        recordList.add(r);

        recordList.add(new Record(125, 76, 80,35,"11.12.2023", "18:30"));
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public Record getRecordById (int id) {
        Record record = null;
        for (Record r : recordList) {
            if (r.getId() == id) {
                record = r;
            }
        }
        return record;
    }
    /*
    public List<Record> getRecord1List() {
        List<Record> recordList1 = new ArrayList<>();
        for (Record r : recordList) {
            recordList1.add(r);
        }
        return recordList1;
    }
    */

}
