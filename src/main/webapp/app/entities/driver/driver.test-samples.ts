import { IDriver, NewDriver } from './driver.model';

export const sampleWithRequiredData: IDriver = {
  id: 10224,
  firstName: 'Alejandro',
  lastName: 'Villegas Delgado',
};

export const sampleWithPartialData: IDriver = {
  id: 26584,
  firstName: 'Rebeca',
  lastName: 'Moreno Castañeda',
};

export const sampleWithFullData: IDriver = {
  id: 6414,
  firstName: 'Cristián',
  lastName: 'Marín Adorno',
};

export const sampleWithNewData: NewDriver = {
  firstName: 'Arturo',
  lastName: 'Vergara Gracia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
