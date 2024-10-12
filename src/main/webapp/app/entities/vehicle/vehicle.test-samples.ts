import { IVehicle, NewVehicle } from './vehicle.model';

export const sampleWithRequiredData: IVehicle = {
  id: 16977,
  model: 1905,
  name: 'however whoa',
};

export const sampleWithPartialData: IVehicle = {
  id: 29126,
  model: 1981,
  name: 'authentic wetly',
};

export const sampleWithFullData: IVehicle = {
  id: 15908,
  model: 2022,
  name: 'embossing density kindly',
};

export const sampleWithNewData: NewVehicle = {
  model: 1966,
  name: 'indeed',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
