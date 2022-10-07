import {Component, OnInit} from '@angular/core';
import {IStudent} from "../student.model";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'jhi-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  student : IStudent | null = null;
  constructor(private route:ActivatedRoute) {
    this.route.data.subscribe(value => {
      this.student = value.student;
    })
  }

  ngOnInit(): void {
    this.route.data.subscribe(value => {
      this.student = value.student;
    })

  }

}
