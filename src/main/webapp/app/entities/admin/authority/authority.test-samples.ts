import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '279fd370-f2c5-4745-b072-40448e53d108',
};

export const sampleWithPartialData: IAuthority = {
  name: 'e01e59c9-c571-4185-b9b0-e49e65951d52',
};

export const sampleWithFullData: IAuthority = {
  name: 'a63ff7b9-d9c0-411c-8ca8-9527297b1f3f',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
