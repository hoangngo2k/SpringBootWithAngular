import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
      {
        path: 'students',
        loadChildren: () => import('./student/student.module').then(st => st.StudentModule)}
    ]),
  ],
})
export class EntityRoutingModule {}
