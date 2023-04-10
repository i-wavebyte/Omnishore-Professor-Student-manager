import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Professor } from 'src/app/_entities/Professor';
import { ProfessorService } from 'src/app/_services/professor-service.service';

@Component({
  selector: 'app-professor-details',
  templateUrl: './professor-details.component.html',
  styleUrls: ['./professor-details.component.css'],
})
export class ProfessorDetailsComponent implements OnInit {
  professor: Professor | null = null;
  professorId: number;

  constructor(
    private route: ActivatedRoute,
    private professorService: ProfessorService
  ) {
    this.professorId = parseInt(this.route.snapshot.paramMap.get('id') || '0', 10);
  }

  ngOnInit(): void {
    this.loadProfessorDetails(this.professorId);
  }

  loadProfessorDetails(id: number): void {
    this.professorService.getProfessor(id).subscribe(
      (professor) => {
        this.professor = professor;
      },
      (error) => {
        console.error('Error fetching professor details:', error);
      }
    );
  }
}
