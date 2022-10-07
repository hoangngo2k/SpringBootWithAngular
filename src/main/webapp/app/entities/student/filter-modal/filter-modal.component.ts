import {Component, OnInit} from '@angular/core';
import {MdbModalRef} from "mdb-angular-ui-kit/modal";

@Component({
  selector: 'jhi-filter-modal',
  templateUrl: './filter-modal.component.html',
  styleUrls: ['./filter-modal.component.scss']
})
export class FilterModalComponent implements OnInit {

  constructor(public modalRef: MdbModalRef<FilterModalComponent>) { }

  ngOnInit(): void {
  }

  saveValue(min: string, max: string) {
    this.modalRef.close(min + "," + max);
  }
}
