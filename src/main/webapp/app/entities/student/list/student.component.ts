import {Component, OnInit, ViewChild} from '@angular/core';
import {StudentService} from "../service/student.service";
import {IStudent} from "../student.model";
import Swal from "sweetalert2";
import {MdbModalRef, MdbModalService} from "mdb-angular-ui-kit/modal";
import {FilterModalComponent} from "../filter-modal/filter-modal.component";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {saveAs} from "file-saver";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'jhi-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.scss']
})
export class StudentComponent implements OnInit {

  // @ts-ignore
  @ViewChild('fileInput') fileInput;

  students: IStudent[] = [];
  name = '';
  min = 0;
  max = 0;
  str: string[] = [];
  ids: number[] = [];
  totalStudents !: number;
  page = 0;
  readonly size = 5;
  field = 'id';
  predicate = true;
  isChecked = false;
  isDeleted = false;
  isLoading = false;

  modalRef: MdbModalRef<FilterModalComponent> | null = null;

  myFiles:string [] = [];
  fileName: string[] = [];
  fileList: FileList | null = null;

  myForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    file: new FormControl('', [Validators.required])
  });

  constructor(private studentService: StudentService, private modalService: MdbModalService) {
  }

  ngOnInit(): void {
    this.studentService.getStudents(this.page, this.size, this.field, this.predicate, '', this.min, this.max).subscribe((res: any) => {
      this.students = res.content;
      this.totalStudents = Number(res.totalElements);
      // this.resetParam();
    })
  }

  openModal() {
    this.modalRef = this.modalService.open(FilterModalComponent);
    this.modalRef.onClose.subscribe((data: any) => {
      this.str = data.split(',');
      this.min = Number(this.str[0]);
      this.max = Number(this.str[1]);
      this.studentService.getStudents(this.page - 1, this.size, this.field, this.predicate, this.name, this.min, this.max).subscribe((res: any) => {
        this.students = res.content;
        this.totalStudents = Number(res.totalElements);
      })
    })
  }

  onClick(event: Event) {
    // @ts-ignore
    this.field = event.srcElement.id;
    this.predicate = !this.predicate;
    this.getStudents(this.page, this.size, this.field, this.predicate, this.name, this.min, this.max);
  }

  refreshStudents() {
    this.name = "";
    this.isLoading = true;
    this.page = 0;
    this.field = 'id';
    this.min = 0;
    this.max = 0;
    this.studentService.getStudents(this.page, this.size, this.field, this.predicate, this.name, this.min, this.max).subscribe((res: any) => {
      this.students = res.content;
      this.isLoading = false;
    })
  }

  getStudents(page: number, size: number, field: string, predicate: boolean, name?: string, min?: number, max?: number) {
    this.isLoading = true;
    this.studentService.getStudents(page, size, field, predicate, name, min, max).subscribe((res: any) => {
      this.students = res.content;
      this.isLoading = false;
    })
  }

  deleteStudent(ids: number[]): void {
    if (ids.length != 0) {
      Swal.fire({
          title: 'Delete id [' + this.ids + ']?',
          icon: 'question',
          showCancelButton: true,
          focusCancel: true,
          showConfirmButton: true
        }
      ).then(value => {
        if (value.isConfirmed) {
          Swal.fire({
            title: 'Delete success',
            icon: 'success',
          }).then(() => {
            this.studentService.delete(ids).subscribe((data: any) => {
              this.isLoading = true;
              this.studentService.getStudents(this.page, this.size, this.field, this.predicate, this.name, this.min, this.max).subscribe((res: any) => {
                this.students = res.content;
                this.isLoading = false;
              })
            });
            this.ids = [];
          })
        }
      })
    } else {
      Swal.fire({
        title: 'You must chose the least one ID',
        icon: 'warning'
      }).then()
    }
  }

  handleChange(e: Event) {
    // @ts-ignore
    if (e.target.checked == true) {
      // @ts-ignore
      this.ids.push(e.target.defaultValue);
    } else {
      this.ids = this.ids.filter(item => {
        // @ts-ignore
        return item != e.target.defaultValue;
      });
    }
    // this.isDeleted = this.ids.length == 0;
  }

  searchStudents(searchName: string) {
    this.name = searchName;
    this.studentService.getStudents(this.page - 1, this.size, this.field, this.predicate, this.name, this.min, this.max).subscribe((res: any) => {
      this.students = res.content;
      this.totalStudents = Number(res.totalElements);
    })
  }

  transition(e: number): void {
    this.page = e - 1;
    this.studentService.getStudents(this.page, this.size, this.field, this.predicate, this.name, this.min, this.max).subscribe((res: any) => {
      this.students = res.content;
    });
  }

  uploadFile() {
    let file = this.fileInput.nativeElement;
    if (file.files[0] instanceof File) {
      // this.studentService.uploadFile(file.files[0]);
    }
  }

  downloadFile(filename: string) {
    if (this.fileName != null) {
      this.studentService.downloadFile(filename).subscribe((data: any) => {
        const byteArray = new Uint8Array(atob(data).split('').map(char => char.charCodeAt(0)))
        saveAs(new Blob([byteArray], {type: "application/pdf, application/docx, application/docx, application/xml"}),
          'signed' + filename);
      });
    }
  }

  get f() {
    return this.myForm.controls;
  }

  onFileChange(event: Event) {
    // @ts-ignore
    for (let i = 0; i < event.target.files.length; i++) {
      // @ts-ignore
      this.myFiles.push(event.target.files[i]);
    }
  }

  submit() {
    const formData = new FormData();
    for (let i = 0; i < this.myFiles.length; i++) {
      formData.append('files', this.myFiles[i]);
    }
    this.studentService.uploadFile(formData);
  }

  getAllFile() {
    this.studentService.getAllFile().subscribe(res => {
      this.fileName = res;
    })
  }
}

