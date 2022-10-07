import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {IStudent} from "../student.model";
import {ApplicationConfigService} from "../../../core/config/application-config.service";

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private resourceUrl = this.applicationConfigService.getEndpointFor('/api/students');
  private fileUrl = this.applicationConfigService.getEndpointFor('/api');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {
  }

  getAll(): Observable<IStudent[]> {
    return this.http.get<IStudent[]>(this.resourceUrl);
  }

  getStudents(page: number, size: number, field: string, predicate: boolean, name ?: string, min?: number, max?: number): Observable<any> {
    let isSort = predicate ? 'asc' : 'desc';
    let paging = '';
    if (name == '' || name == null)
      paging = "page=" + page + "&size=" + size + "&sort=" + field + "," + isSort + "&min=" + min + "&max=" + max;
    else
      paging = "page=" + page + "&size=" + size + "&sort=" + field + "," + isSort + "&search=" + name + "&min=" + min + "&max=" + max;
    return this.http.get<any>(this.resourceUrl + '/paging?' + paging);
  }

  getById(id: number): Observable<IStudent> {
    return this.http.get<IStudent>(this.resourceUrl + '/' + id);
  }

  create(student: any): Observable<IStudent> {
    return this.http.post<IStudent>(this.resourceUrl, student);
  }

  update(student: any, id: number | undefined): Observable<IStudent> {
    return this.http.put<IStudent>(this.resourceUrl + '/' + id, student);
  }

  delete(ids: number[]): Observable<string> {
    return this.http.delete<string>(`${this.resourceUrl}/${ids}`);
  }

  getAllFile(): Observable<any> {
    return this.http.get(this.fileUrl + '/files')
  }

  uploadFile(formData: FormData) {
    this.http.post(this.fileUrl + '/upload', formData, {
      reportProgress: true,
      responseType: 'text',
      headers: {'Accept': 'application/pdf, application/xml, application/docx'}
    }).subscribe(res => {
      console.log(res)
      alert('Upload file success!');
    });
  }

  downloadFile(filename: string): Observable<any> {
    // @ts-ignore
    return this.http.get<any>(this.fileUrl + '/files/' + filename, {responseType: 'text'});
  }
}
