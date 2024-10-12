import { IVehicleBrand, NewVehicleBrand } from './vehicle-brand.model';

export const sampleWithRequiredData: IVehicleBrand = {
  id: 11869,
  name: 'when meh',
};

export const sampleWithPartialData: IVehicleBrand = {
  id: 11294,
  name: 'priesthood why implode',
};

export const sampleWithFullData: IVehicleBrand = {
  id: 27851,
  name: 'pfft although',
};

export const sampleWithNewData: NewVehicleBrand = {
  name: 'hmph',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
