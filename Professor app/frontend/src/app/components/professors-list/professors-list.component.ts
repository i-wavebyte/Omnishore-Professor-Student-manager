import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PageResponse } from 'src/app/_entities/PageResponse';
import { Professor } from 'src/app/_entities/Professor';
import { ProfessorService } from 'src/app/_services/professor-service.service';

@Component({
  selector: 'app-professors-list',
  templateUrl: './professors-list.component.html',
  styleUrls: ['./professors-list.component.css'],
})
export class ProfessorsListComponent implements OnInit {
  professors: Professor[] = [];
  filteredProfessors: Professor[] = [];
  subjects: string[] = [];
  searchValue: string = '';
  selectedSubject: string = '';
  nameOrder: string = '';
  page: number = 0;
  pageSize: number = 10;
  totalProfessors!: number;

  constructor(private professorService: ProfessorService, private router: Router) {}

  ngOnInit(): void {
    this.loadProfessors();
    this.loadSubjects();
  }

  
  loadProfessors(): void {
    this.professorService
        .getProfessorsPage(
            this.page,
            this.pageSize,
            'name',
            this.nameOrder,
            this.searchValue,
            this.selectedSubject
        )
        .subscribe(
            (response: PageResponse<Professor>) => {
                this.professors = response.content;
                this.filteredProfessors = response.content;
                this.totalProfessors = response.totalElements;
            },
            (error) => {
                console.error('Error fetching professors:', error);
            }
        );
}

  loadSubjects() {
    this.professorService.getAllUniqueSubjects().subscribe((subjects) => {
      this.subjects = subjects;
    });
  }

  onNameOrderChange(order: string): void {
    this.nameOrder = order;
    this.page=0;
    this.loadProfessors();

  }


  onSubjectFilterChange(subject: string): void {
    this.selectedSubject = subject;
    this.page=0;
    this.loadProfessors();
  }
  
  onSearch(): void {
    this.page=0;
    this.loadProfessors();
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadProfessors();
    }
  }

  onInfoProfessor(profId: number): void {
    this.router.navigate(['/details', profId]);
  }

  nextPage(): void {
    console.log('nextPage');
    
    if ((this.page + 1) * this.pageSize < this.totalProfessors) {
      this.page++;
      this.loadProfessors();
    }
  }

  onEditProfessor(professor: Professor): void {
    this.professorService.updateProfessor(professor.id, professor).subscribe(
      (updatedProfessor) => {
        console.log('Professor updated:', updatedProfessor);
      },
      (error) => {
        console.error('Error updating professor:', error);
      }
    );
  }

  onDeleteProfessor(profId: number): void {
    this.professorService.deleteProfessor(profId).subscribe(
      () => {
        this.professors = this.professors.filter(
          (professor) => professor.id !== profId
        );
        this.filteredProfessors = this.filteredProfessors.filter(
          (professor) => professor.id !== profId
        );
        console.log('Professor deleted:', profId);
        this.loadProfessors();
      },
      (error) => {
        console.error('Error deleting professor:', error);
      }
    );
  }
}