import {Component, OnInit} from '@angular/core';
import {IStudent} from "../student.model";
import {ActivatedRoute} from "@angular/router";
import {StudentService} from "../service/student.service";
import {FormBuilder} from "@angular/forms";
import Swal from "sweetalert2";

@Component({
  selector: 'jhi-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit {

  students : IStudent | null = null;
  isSaving = false;
  editForm = this.formGroup.group({
    id: this.students?.id,
    code: this.students?.code,
    fullName: this.students?.fullName,
    gender: this.students?.gender,
    grade: this.students?.grade,
    point: this.students?.point
  })

  constructor(private route:ActivatedRoute,
              private studentService:StudentService,
              private formGroup:FormBuilder,
  ) { }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.students = data.student
      if (data) {
        // @ts-ignore
        this.editForm.reset(this.students)
      }
    })
  }

  save() {
    this.isSaving = true;
    // @ts-ignore
    this.students = this.editForm.getRawValue();
    if (this.students?.id != null) {
      this.studentService.update(this.students, this.students?.id).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError()
      });
    } else {
      this.studentService.create(this.students).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError()
      })
    }
  }

  previousState() {
    window.history.back();
  }

  private onSaveSuccess(): void {
    Swal.fire({
      title: 'Save success!',
      icon: 'success',
      showConfirmButton: true,
    }).then(value => {
      if (value.isConfirmed)
        this.previousState()
    });
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
