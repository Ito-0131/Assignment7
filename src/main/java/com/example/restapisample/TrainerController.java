package com.example.restapisample;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//【課題】HTTPメソッドのGET/POST/PATCH/DELETE のリクエストを扱えるController を実装
@RestController
@RequestMapping("/trainer")
@Validated
public class TrainerController {

    private List<StudentForm> studentList = new ArrayList<>();

    public TrainerController() {
        studentList.add(new StudentForm("10000", "Haruto", "ラウドボーン", LocalDate.of(2008, 1, 31)));
        studentList.add(new StudentForm("10001", "Nemo", "Masquernya", LocalDate.of(2008, 3, 18)));
        studentList.add(new StudentForm("10002", "Botan", "Nymphia", LocalDate.of(2008, 7, 30)));
    }

    @GetMapping
    public ResponseEntity<List<StudentForm>> getStudents() {
        return ResponseEntity.ok(studentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentForm> getStudent(@PathVariable String id) {
        StudentForm student = studentList.stream()
                .filter(s -> s.getStudentId().equals(id))
                .findFirst()
                .orElse(null);

        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student-info")
    public ResponseEntity<String> getStudentInfo(@RequestParam("id") String id) {
        // IDに紐づく生徒を検索する
        Optional<StudentForm> optionalStudent = studentList.stream()
                .filter(s -> s.getStudentId().equals(id))
                .findFirst();

        if (optionalStudent.isPresent()) {
            StudentForm student = optionalStudent.get();
            String message = student.getName() + "さんの誕生日は" + student.getBirthday() + "で、相棒は" + student.getPartnerPokemon() + "です";
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/names")
    public ResponseEntity<String> getNames(@RequestParam(value = "name", required = false) String name) {
        // クエリ文字列の"name"パラメータが20文字より多いか、nullの場合にエラーメッセージを表示させる
        if (name == null || name.length() > 20) {
            return ResponseEntity.badRequest().body("Name is more than 20 characters or has not been entered.");
        }

        // クエリ文字列の"name"パラメータを受け取って処理する
        return ResponseEntity.ok("RaiseTechへようこそ！ " + name + "さん！");
    }

    @GetMapping("/birthdays")
    public ResponseEntity<String> getBirthdays(@RequestParam(value = "birthday", required = false) String birthday) {
        // クエリ文字列の"birthday"パラメータがnullの場合にエラーメッセージを表示させる
        if (birthday == null) {
            return ResponseEntity.badRequest().body("Birthday has not been entered.");
        }

        // クエリ文字列の"birthday"パラメータを受け取って処理する
        return ResponseEntity.ok("あなたの誕生日は " + birthday + "ですね");
    }


    @PostMapping
    public ResponseEntity<StudentForm> createStudent(@Valid @RequestBody StudentForm studentForm) {
        studentList.add(studentForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentForm);
    }

    @PatchMapping("/{id}")
    public StudentUpdate updateStudent(@PathVariable int id, @Valid @RequestBody StudentUpdate studentUpdate) {

        return studentUpdate;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        boolean isRemoved = studentList.removeIf(student -> student.getStudentId().equals(id));

        if (isRemoved) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}







