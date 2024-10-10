import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISleepDetector } from '../sleep-detector.model';
import { SleepDetectorService } from '../service/sleep-detector.service';

const sleepDetectorResolve = (route: ActivatedRouteSnapshot): Observable<null | ISleepDetector> => {
  const id = route.params.id;
  if (id) {
    return inject(SleepDetectorService)
      .find(id)
      .pipe(
        mergeMap((sleepDetector: HttpResponse<ISleepDetector>) => {
          if (sleepDetector.body) {
            return of(sleepDetector.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default sleepDetectorResolve;
