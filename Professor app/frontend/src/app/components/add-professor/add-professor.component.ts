// add-professor.component.ts
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Professor } from 'src/app/_entities/Professor';
import { ProfessorService } from 'src/app/_services/professor-service.service';

@Component({
  selector: 'app-add-professor',
  templateUrl: './add-professor.component.html',
  styleUrls: ['./add-professor.component.css'],
})
export class AddProfessorComponent implements OnInit {
  addProfessorForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private professorService: ProfessorService
  ) {
    this.createForm();
  }

  ngOnInit(): void {}

  createForm() {
    this.addProfessorForm = this.fb.group({
      name: ['', Validators.required],
      cin: ['', Validators.required],
      subject: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.addProfessorForm.valid) {
      const newProfessor: Professor = this.addProfessorForm.value;
      this.professorService.addProfessor(newProfessor).subscribe((professor) => {
        console.log('Professor added successfully', professor);
        this.addProfessorForm.reset();
      });
    }
  }
}