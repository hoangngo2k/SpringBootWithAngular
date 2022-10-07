import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, Routes} from "@angular/router";
import {Observable, of} from "rxjs";
import {StudentComponent} from "./list/student.component";
import {DetailComponent} from "./detail/detail.component";
import {IStudent} from "./student.model";
import {StudentService} from "./service/student.service";
import {UpdateComponent} from "./update/update.component";

@Injectable({ providedIn: 'root' })
export class StudentManagementResolve implements Resolve<IStudent | null> {
  constructor(private service: StudentService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudent | null> {
    const id = route.params['id'];
    if (id) {
      return this.service.getById(id);
    }
    return of(null);
  }
}

export const studentRoute: Routes = [
  {
    path:'',
    component: StudentComponent,
  },
  {
    path:'view/:id',
    component: DetailComponent,
    resolve: {
      student: StudentManagementResolve
    },
  },
  {
    path:'new',
    component: UpdateComponent,
    resolve:{
      student: StudentManagementResolve
    }
  },
  {
    path:'edit/:id',
    component: UpdateComponent,
    resolve: {
      student: StudentManagementResolve
    }
  },
]
