import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Professor } from '../_entities/Professor';
import { PageResponse } from '../_entities/PageResponse';
@Injectable({
  providedIn: 'root',
})
export class ProfessorService {
  private baseUrl = 'http://localhost:8080/professorService';

  constructor(private http: HttpClient) {}

  getProfessor(profId: number): Observable<Professor> {
    return this.http.get<Professor>(`${this.baseUrl}/get/${profId}`);
  }

  getAllProfessors(): Observable<Professor[]> {
    return this.http.get<Professor[]>(`${this.baseUrl}/get`);
  }

  getProfessorList(profIds: number[]): Observable<Professor[]> {
    return this.http.get<Professor[]>(`${this.baseUrl}/get/list`, {
      params: { profIds },
    });
  }

  addProfessor(professor: Professor): Observable<Professor> {
    return this.http.post<Professor>(`${this.baseUrl}/add`, professor);
  }

  updateProfessor(profId: number, professor: Professor): Observable<Professor> {
    return this.http.put<Professor>(
      `${this.baseUrl}/update/${profId}`,
      professor
    );
  }

  deleteProfessor(profId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${profId}`);
  }

  assignStudent(profId: number, studentId: number): Observable<Professor> {
    return this.http.put<Professor>(`${this.baseUrl}/assign/${profId}`, null, {
      params: { studentId },
    });
  }

  assignStudents(profId: number, studentIds: number[]): Observable<Professor> {
    return this.http.put<Professor>(
      `${this.baseUrl}/assignAll/${profId}`,
      studentIds
    );
  }

  addAll(professors: Professor[]): Observable<Professor[]> {
    return this.http.post<Professor[]>(`${this.baseUrl}/addAll`, professors);
  }

  getAllUniqueSubjects(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/subjects`);
  }

  getProfessorsPage(
    page: number,
    size: number,
    sortBy: string,
    sortOrder: string,
    searchQuery: string,
    subjectFilter: string
  ): Observable<PageResponse<Professor>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortOrder', sortOrder);

    if (searchQuery) {
      params = params.set('searchQuery', searchQuery);
    }

    if (subjectFilter) {
      params = params.set('subjectFilter', subjectFilter);
    }

    return this.http.get<PageResponse<Professor>>(
      `${this.baseUrl}/get`,
      { params }
    );
  }
}
