import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vehicle-type.test-samples';

import { VehicleTypeFormService } from './vehicle-type-form.service';

describe('VehicleType Form Service', () => {
  let service: VehicleTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleTypeFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IVehicleType should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicleType', () => {
      it('should return NewVehicleType for default VehicleType initial value', () => {
        const formGroup = service.createVehicleTypeFormGroup(sampleWithNewData);

        const vehicleType = service.getVehicleType(formGroup) as any;

        expect(vehicleType).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicleType for empty VehicleType initial value', () => {
        const formGroup = service.createVehicleTypeFormGroup();

        const vehicleType = service.getVehicleType(formGroup) as any;

        expect(vehicleType).toMatchObject({});
      });

      it('should return IVehicleType', () => {
        const formGroup = service.createVehicleTypeFormGroup(sampleWithRequiredData);

        const vehicleType = service.getVehicleType(formGroup) as any;

        expect(vehicleType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicleType should not enable id FormControl', () => {
        const formGroup = service.createVehicleTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicleType should disable id FormControl', () => {
        const formGroup = service.createVehicleTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
