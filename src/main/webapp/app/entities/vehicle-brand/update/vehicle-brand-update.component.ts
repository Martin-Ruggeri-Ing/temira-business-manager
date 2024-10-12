import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVehicleBrand } from '../vehicle-brand.model';
import { VehicleBrandService } from '../service/vehicle-brand.service';
import { VehicleBrandFormGroup, VehicleBrandFormService } from './vehicle-brand-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-brand-update',
  templateUrl: './vehicle-brand-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleBrandUpdateComponent implements OnInit {
  isSaving = false;
  vehicleBrand: IVehicleBrand | null = null;

  protected vehicleBrandService = inject(VehicleBrandService);
  protected vehicleBrandFormService = inject(VehicleBrandFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehicleBrandFormGroup = this.vehicleBrandFormService.createVehicleBrandFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleBrand }) => {
      this.vehicleBrand = vehicleBrand;
      if (vehicleBrand) {
        this.updateForm(vehicleBrand);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicleBrand = this.vehicleBrandFormService.getVehicleBrand(this.editForm);
    if (vehicleBrand.id !== null) {
      this.subscribeToSaveResponse(this.vehicleBrandService.update(vehicleBrand));
    } else {
      this.subscribeToSaveResponse(this.vehicleBrandService.create(vehicleBrand));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleBrand>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vehicleBrand: IVehicleBrand): void {
    this.vehicleBrand = vehicleBrand;
    this.vehicleBrandFormService.resetForm(this.editForm, vehicleBrand);
  }
}
