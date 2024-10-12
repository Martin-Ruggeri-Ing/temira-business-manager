import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VehicleBrandResolve from './route/vehicle-brand-routing-resolve.service';

const vehicleBrandRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vehicle-brand.component').then(m => m.VehicleBrandComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vehicle-brand-detail.component').then(m => m.VehicleBrandDetailComponent),
    resolve: {
      vehicleBrand: VehicleBrandResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vehicle-brand-update.component').then(m => m.VehicleBrandUpdateComponent),
    resolve: {
      vehicleBrand: VehicleBrandResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vehicle-brand-update.component').then(m => m.VehicleBrandUpdateComponent),
    resolve: {
      vehicleBrand: VehicleBrandResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vehicleBrandRoute;
