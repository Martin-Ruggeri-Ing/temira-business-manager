import { IDriver, NewDriver } from './driver.model';

export const sampleWithRequiredData: IDriver = {
  id: 15900,
  firstName: 'Francisco',
  lastName: 'Nevárez Griego',
};

export const sampleWithPartialData: IDriver = {
  id: 29298,
  firstName: 'Luisa',
  lastName: 'Mondragón Murillo',
};

export const sampleWithFullData: IDriver = {
  id: 30259,
  firstName: 'Carolina',
  lastName: 'Farías Loera',
};

export const sampleWithNewData: NewDriver = {
  firstName: 'Teodoro',
  lastName: 'Espinosa de los Monteros Ureña',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
