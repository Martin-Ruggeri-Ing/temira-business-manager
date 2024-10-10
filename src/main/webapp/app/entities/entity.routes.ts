import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'temiraApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'vehicle',
    data: { pageTitle: 'temiraApp.vehicle.home.title' },
    loadChildren: () => import('./vehicle/vehicle.routes'),
  },
  {
    path: 'driver',
    data: { pageTitle: 'temiraApp.driver.home.title' },
    loadChildren: () => import('./driver/driver.routes'),
  },
  {
    path: 'sleep-detector',
    data: { pageTitle: 'temiraApp.sleepDetector.home.title' },
    loadChildren: () => import('./sleep-detector/sleep-detector.routes'),
  },
  {
    path: 'statement',
    data: { pageTitle: 'temiraApp.statement.home.title' },
    loadChildren: () => import('./statement/statement.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
