import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../sleep-detector.test-samples';

import { SleepDetectorFormService } from './sleep-detector-form.service';

describe('SleepDetector Form Service', () => {
  let service: SleepDetectorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SleepDetectorFormService);
  });

  describe('Service methods', () => {
    describe('createSleepDetectorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSleepDetectorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing ISleepDetector should create a new form with FormGroup', () => {
        const formGroup = service.createSleepDetectorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getSleepDetector', () => {
      it('should return NewSleepDetector for default SleepDetector initial value', () => {
        const formGroup = service.createSleepDetectorFormGroup(sampleWithNewData);

        const sleepDetector = service.getSleepDetector(formGroup) as any;

        expect(sleepDetector).toMatchObject(sampleWithNewData);
      });

      it('should return NewSleepDetector for empty SleepDetector initial value', () => {
        const formGroup = service.createSleepDetectorFormGroup();

        const sleepDetector = service.getSleepDetector(formGroup) as any;

        expect(sleepDetector).toMatchObject({});
      });

      it('should return ISleepDetector', () => {
        const formGroup = service.createSleepDetectorFormGroup(sampleWithRequiredData);

        const sleepDetector = service.getSleepDetector(formGroup) as any;

        expect(sleepDetector).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISleepDetector should not enable id FormControl', () => {
        const formGroup = service.createSleepDetectorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSleepDetector should disable id FormControl', () => {
        const formGroup = service.createSleepDetectorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
