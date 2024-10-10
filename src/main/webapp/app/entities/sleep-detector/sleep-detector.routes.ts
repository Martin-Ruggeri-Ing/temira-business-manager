import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SleepDetectorResolve from './route/sleep-detector-routing-resolve.service';

const sleepDetectorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/sleep-detector.component').then(m => m.SleepDetectorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/sleep-detector-detail.component').then(m => m.SleepDetectorDetailComponent),
    resolve: {
      sleepDetector: SleepDetectorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/sleep-detector-update.component').then(m => m.SleepDetectorUpdateComponent),
    resolve: {
      sleepDetector: SleepDetectorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/sleep-detector-update.component').then(m => m.SleepDetectorUpdateComponent),
    resolve: {
      sleepDetector: SleepDetectorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sleepDetectorRoute;
