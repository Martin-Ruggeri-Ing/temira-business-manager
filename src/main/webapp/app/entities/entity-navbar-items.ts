import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'VehicleType',
    route: '/vehicle-type',
    translationKey: 'global.menu.entities.vehicleType',
  },
  {
    name: 'VehicleBrand',
    route: '/vehicle-brand',
    translationKey: 'global.menu.entities.vehicleBrand',
  },
  {
    name: 'Vehicle',
    route: '/vehicle',
    translationKey: 'global.menu.entities.vehicle',
  },
  {
    name: 'Driver',
    route: '/driver',
    translationKey: 'global.menu.entities.driver',
  },
  {
    name: 'SleepDetector',
    route: '/sleep-detector',
    translationKey: 'global.menu.entities.sleepDetector',
  },
  {
    name: 'Statement',
    route: '/statement',
    translationKey: 'global.menu.entities.statement',
  },
];
