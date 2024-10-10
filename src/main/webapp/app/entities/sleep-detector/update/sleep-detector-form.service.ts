import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISleepDetector, NewSleepDetector } from '../sleep-detector.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISleepDetector for edit and NewSleepDetectorFormGroupInput for create.
 */
type SleepDetectorFormGroupInput = ISleepDetector | PartialWithRequiredKeyOf<NewSleepDetector>;

type SleepDetectorFormDefaults = Pick<NewSleepDetector, 'id'>;

type SleepDetectorFormGroupContent = {
  id: FormControl<ISleepDetector['id'] | NewSleepDetector['id']>;
  vehicle: FormControl<ISleepDetector['vehicle']>;
  driver: FormControl<ISleepDetector['driver']>;
  user: FormControl<ISleepDetector['user']>;
};

export type SleepDetectorFormGroup = FormGroup<SleepDetectorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SleepDetectorFormService {
  createSleepDetectorFormGroup(sleepDetector: SleepDetectorFormGroupInput = { id: null }): SleepDetectorFormGroup {
    const sleepDetectorRawValue = {
      ...this.getFormDefaults(),
      ...sleepDetector,
    };
    return new FormGroup<SleepDetectorFormGroupContent>({
      id: new FormControl(
        { value: sleepDetectorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vehicle: new FormControl(sleepDetectorRawValue.vehicle),
      driver: new FormControl(sleepDetectorRawValue.driver),
      user: new FormControl(sleepDetectorRawValue.user),
    });
  }

  getSleepDetector(form: SleepDetectorFormGroup): ISleepDetector | NewSleepDetector {
    return form.getRawValue() as ISleepDetector | NewSleepDetector;
  }

  resetForm(form: SleepDetectorFormGroup, sleepDetector: SleepDetectorFormGroupInput): void {
    const sleepDetectorRawValue = { ...this.getFormDefaults(), ...sleepDetector };
    form.reset(
      {
        ...sleepDetectorRawValue,
        id: { value: sleepDetectorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SleepDetectorFormDefaults {
    return {
      id: null,
    };
  }
}
