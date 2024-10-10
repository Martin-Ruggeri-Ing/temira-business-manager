import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStatement } from '../statement.model';
import { StatementService } from '../service/statement.service';

const statementResolve = (route: ActivatedRouteSnapshot): Observable<null | IStatement> => {
  const id = route.params.id;
  if (id) {
    return inject(StatementService)
      .find(id)
      .pipe(
        mergeMap((statement: HttpResponse<IStatement>) => {
          if (statement.body) {
            return of(statement.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default statementResolve;
