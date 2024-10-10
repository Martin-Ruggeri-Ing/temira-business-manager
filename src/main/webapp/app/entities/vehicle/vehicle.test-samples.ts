import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: 13720,
  model: 2045,
};

export const sampleWithPartialData: IVehicle = {
  id: 23710,
  model: 1907,
  type: 'Casas',
};

export const sampleWithFullData: IVehicle = {
  id: 7177,
  model: 1997,
  type: 'Furgon',
  brand: 'Volkswagen',
};

export const sampleWithNewData: NewVehicle = {
  model: 1935,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
