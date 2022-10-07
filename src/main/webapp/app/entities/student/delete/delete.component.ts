import { Component, OnInit } from '@angular/core';
import {Student} from "../student.model";
import {StudentService} from "../service/student.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss']
})
export class DeleteComponent implements OnInit {

  ids : number[] = [];

  constructor(private studentService: StudentService, private activeModal:NgbActiveModal) { }

  ngOnInit(): void {
  }

  cancel() {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number[]) {
    // this.studentService.delete(id).subscribe(() => {
    //   this.activeModal.close('deleted');
    // })
  }

}
