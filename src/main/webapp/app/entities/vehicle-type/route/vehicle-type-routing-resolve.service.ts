import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicleType } from '../vehicle-type.model';
import { VehicleTypeService } from '../service/vehicle-type.service';

const vehicleTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IVehicleType> => {
  const id = route.params.id;
  if (id) {
    return inject(VehicleTypeService)
      .find(id)
      .pipe(
        mergeMap((vehicleType: HttpResponse<IVehicleType>) => {
          if (vehicleType.body) {
            return of(vehicleType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vehicleTypeResolve;
