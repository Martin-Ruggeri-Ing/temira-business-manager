import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'temiraBusinessManagerApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'vehicle-type',
    data: { pageTitle: 'temiraBusinessManagerApp.vehicleType.home.title' },
    loadChildren: () => import('./vehicle-type/vehicle-type.routes'),
  },
  {
    path: 'vehicle-brand',
    data: { pageTitle: 'temiraBusinessManagerApp.vehicleBrand.home.title' },
    loadChildren: () => import('./vehicle-brand/vehicle-brand.routes'),
  },
  {
    path: 'vehicle',
    data: { pageTitle: 'temiraBusinessManagerApp.vehicle.home.title' },
    loadChildren: () => import('./vehicle/vehicle.routes'),
  },
  {
    path: 'driver',
    data: { pageTitle: 'temiraBusinessManagerApp.driver.home.title' },
    loadChildren: () => import('./driver/driver.routes'),
  },
  {
    path: 'sleep-detector',
    data: { pageTitle: 'temiraBusinessManagerApp.sleepDetector.home.title' },
    loadChildren: () => import('./sleep-detector/sleep-detector.routes'),
  },
  {
    path: 'statement',
    data: { pageTitle: 'temiraBusinessManagerApp.statement.home.title' },
    loadChildren: () => import('./statement/statement.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
