import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VehicleTypeResolve from './route/vehicle-type-routing-resolve.service';

const vehicleTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vehicle-type.component').then(m => m.VehicleTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vehicle-type-detail.component').then(m => m.VehicleTypeDetailComponent),
    resolve: {
      vehicleType: VehicleTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vehicle-type-update.component').then(m => m.VehicleTypeUpdateComponent),
    resolve: {
      vehicleType: VehicleTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vehicle-type-update.component').then(m => m.VehicleTypeUpdateComponent),
    resolve: {
      vehicleType: VehicleTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vehicleTypeRoute;
