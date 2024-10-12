import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vehicle-brand.test-samples';

import { VehicleBrandFormService } from './vehicle-brand-form.service';

describe('VehicleBrand Form Service', () => {
  let service: VehicleBrandFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleBrandFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleBrandFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleBrandFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IVehicleBrand should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleBrandFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicleBrand', () => {
      it('should return NewVehicleBrand for default VehicleBrand initial value', () => {
        const formGroup = service.createVehicleBrandFormGroup(sampleWithNewData);

        const vehicleBrand = service.getVehicleBrand(formGroup) as any;

        expect(vehicleBrand).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicleBrand for empty VehicleBrand initial value', () => {
        const formGroup = service.createVehicleBrandFormGroup();

        const vehicleBrand = service.getVehicleBrand(formGroup) as any;

        expect(vehicleBrand).toMatchObject({});
      });

      it('should return IVehicleBrand', () => {
        const formGroup = service.createVehicleBrandFormGroup(sampleWithRequiredData);

        const vehicleBrand = service.getVehicleBrand(formGroup) as any;

        expect(vehicleBrand).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicleBrand should not enable id FormControl', () => {
        const formGroup = service.createVehicleBrandFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicleBrand should disable id FormControl', () => {
        const formGroup = service.createVehicleBrandFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
