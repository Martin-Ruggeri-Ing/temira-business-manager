import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 11843,
  login: 'Sec',
};

export const sampleWithPartialData: IUser = {
  id: 2141,
  login: 'zkfc',
};

export const sampleWithFullData: IUser = {
  id: 26099,
  login: 'n',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
