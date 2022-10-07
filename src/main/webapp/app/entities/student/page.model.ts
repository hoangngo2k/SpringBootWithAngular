export interface PageModel {
  title?: string,
  page: number,
  size: number,
  field: string,
  predicate: boolean,
}
export class PageModelCl implements PageModel{
  constructor(public page: number,
              public size: number,
              public field: string,
              public predicate: boolean) {
  }
}
