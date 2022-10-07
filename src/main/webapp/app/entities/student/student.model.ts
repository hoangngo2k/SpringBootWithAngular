export interface IStudent {
  id:number,
  code:string,
  fullName:string,
  gender:string,
  grade:string,
  point:number
}

export class Student implements IStudent {
  constructor(public id:number, public code:string, public fullName:string, public gender:string, public grade:string, public point:number) {
  }
}
