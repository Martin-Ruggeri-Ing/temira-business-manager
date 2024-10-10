import { ISleepDetector, NewSleepDetector } from './sleep-detector.model';

export const sampleWithRequiredData: ISleepDetector = {
  id: 26163,
};

export const sampleWithPartialData: ISleepDetector = {
  id: 9946,
};

export const sampleWithFullData: ISleepDetector = {
  id: 231,
};

export const sampleWithNewData: NewSleepDetector = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
