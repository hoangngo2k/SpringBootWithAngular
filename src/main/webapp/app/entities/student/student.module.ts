import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {DetailComponent} from './detail/detail.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {studentRoute} from "./student.route";
import {UpdateComponent} from './update/update.component';
import {SharedModule} from "../../shared/shared.module";
import {DeleteComponent} from './delete/delete.component';
import {FilterModalComponent} from './filter-modal/filter-modal.component';

@NgModule({
    imports: [
        RouterModule.forChild(studentRoute),
        FontAwesomeModule,
        SharedModule
    ],
    declarations: [
        DetailComponent,
        UpdateComponent,
        DeleteComponent,
        FilterModalComponent
    ],
    exports: [
        FilterModalComponent
    ]
})

export class StudentModule {
}
