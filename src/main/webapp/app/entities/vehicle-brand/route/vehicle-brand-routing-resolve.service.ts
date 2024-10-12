import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicleBrand } from '../vehicle-brand.model';
import { VehicleBrandService } from '../service/vehicle-brand.service';

const vehicleBrandResolve = (route: ActivatedRouteSnapshot): Observable<null | IVehicleBrand> => {
  const id = route.params.id;
  if (id) {
    return inject(VehicleBrandService)
      .find(id)
      .pipe(
        mergeMap((vehicleBrand: HttpResponse<IVehicleBrand>) => {
          if (vehicleBrand.body) {
            return of(vehicleBrand.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vehicleBrandResolve;
