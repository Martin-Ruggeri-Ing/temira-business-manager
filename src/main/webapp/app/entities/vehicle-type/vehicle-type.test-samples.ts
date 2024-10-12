import { IVehicleType, NewVehicleType } from './vehicle-type.model';

export const sampleWithRequiredData: IVehicleType = {
  id: 29747,
  name: 'hence ew indolent',
};

export const sampleWithPartialData: IVehicleType = {
  id: 13605,
  name: 'excitable honestly notwithstanding',
};

export const sampleWithFullData: IVehicleType = {
  id: 964,
  name: 'providence loyally',
};

export const sampleWithNewData: NewVehicleType = {
  name: 'exacerbate separately',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
