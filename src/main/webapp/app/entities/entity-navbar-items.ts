import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
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
