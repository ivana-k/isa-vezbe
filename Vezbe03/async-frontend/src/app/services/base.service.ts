import { HttpHeaders, HttpParams, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { throwError } from "rxjs";

/**
 * Every Http service should extend this class.
 * Because all Http services share same url and some other methods.
 */
 @Injectable({
    providedIn: 'root'
  })
export abstract class BaseService {

  static appUrl = 'defaultUrl';

  constructor() {
  }

  logError(error: HttpResponse<any>) {
    try {
      const errorData = error.body;
      console.error(errorData.error);
    } catch (e) {
      console.error(error);
    }
    throw throwError(error);
  }

  protected getOptions(params1?: HttpParams) {
    let headers = new HttpHeaders();
    headers = headers.set('Content-type', 'application/json');
    headers = headers.set('Accept', 'application/json');
    if (params1) {
      return {
        params: params1,
        headers: headers
      };
    }
    return {
      headers: headers
    };
  }

}
