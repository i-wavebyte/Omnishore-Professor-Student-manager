// student.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Student } from '../_entities/Student';


@Injectable({
  providedIn: 'root',
})
export class StudentService {
  private baseUrl = 'http://localhost:8080/studentService';

  constructor(private http: HttpClient) {}

  getById(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.baseUrl}/find/${id}`);
  }

  getAllStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/findAll`);
  }

  getStudentsByList(studentIds: number[]): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/getIdsList`, {
      params: { studentIds },
    });
  }

  assignProfessor(studentId: number, profId: number): Observable<Student> {
    return this.http.put<Student>(`${this.baseUrl}/assignProfessor/${studentId}`, null, {
      params: { profId },
    });
  }
}