import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import StatementResolve from './route/statement-routing-resolve.service';

const statementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/statement.component').then(m => m.StatementComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/statement-detail.component').then(m => m.StatementDetailComponent),
    resolve: {
      statement: StatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/statement-update.component').then(m => m.StatementUpdateComponent),
    resolve: {
      statement: StatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/statement-update.component').then(m => m.StatementUpdateComponent),
    resolve: {
      statement: StatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default statementRoute;
