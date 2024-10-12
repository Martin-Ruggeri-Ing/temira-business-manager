import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVehicleBrand, NewVehicleBrand } from '../vehicle-brand.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicleBrand for edit and NewVehicleBrandFormGroupInput for create.
 */
type VehicleBrandFormGroupInput = IVehicleBrand | PartialWithRequiredKeyOf<NewVehicleBrand>;

type VehicleBrandFormDefaults = Pick<NewVehicleBrand, 'id'>;

type VehicleBrandFormGroupContent = {
  id: FormControl<IVehicleBrand['id'] | NewVehicleBrand['id']>;
  name: FormControl<IVehicleBrand['name']>;
};

export type VehicleBrandFormGroup = FormGroup<VehicleBrandFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleBrandFormService {
  createVehicleBrandFormGroup(vehicleBrand: VehicleBrandFormGroupInput = { id: null }): VehicleBrandFormGroup {
    const vehicleBrandRawValue = {
      ...this.getFormDefaults(),
      ...vehicleBrand,
    };
    return new FormGroup<VehicleBrandFormGroupContent>({
      id: new FormControl(
        { value: vehicleBrandRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(vehicleBrandRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getVehicleBrand(form: VehicleBrandFormGroup): IVehicleBrand | NewVehicleBrand {
    return form.getRawValue() as IVehicleBrand | NewVehicleBrand;
  }

  resetForm(form: VehicleBrandFormGroup, vehicleBrand: VehicleBrandFormGroupInput): void {
    const vehicleBrandRawValue = { ...this.getFormDefaults(), ...vehicleBrand };
    form.reset(
      {
        ...vehicleBrandRawValue,
        id: { value: vehicleBrandRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleBrandFormDefaults {
    return {
      id: null,
    };
  }
}
