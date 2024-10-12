import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VehicleTypeService } from '../service/vehicle-type.service';
import { IVehicleType } from '../vehicle-type.model';
import { VehicleTypeFormService } from './vehicle-type-form.service';

import { VehicleTypeUpdateComponent } from './vehicle-type-update.component';

describe('VehicleType Management Update Component', () => {
  let comp: VehicleTypeUpdateComponent;
  let fixture: ComponentFixture<VehicleTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleTypeFormService: VehicleTypeFormService;
  let vehicleTypeService: VehicleTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VehicleTypeUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VehicleTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleTypeFormService = TestBed.inject(VehicleTypeFormService);
    vehicleTypeService = TestBed.inject(VehicleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vehicleType: IVehicleType = { id: 456 };

      activatedRoute.data = of({ vehicleType });
      comp.ngOnInit();

      expect(comp.vehicleType).toEqual(vehicleType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleType>>();
      const vehicleType = { id: 123 };
      jest.spyOn(vehicleTypeFormService, 'getVehicleType').mockReturnValue(vehicleType);
      jest.spyOn(vehicleTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleType }));
      saveSubject.complete();

      // THEN
      expect(vehicleTypeFormService.getVehicleType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleTypeService.update).toHaveBeenCalledWith(expect.objectContaining(vehicleType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleType>>();
      const vehicleType = { id: 123 };
      jest.spyOn(vehicleTypeFormService, 'getVehicleType').mockReturnValue({ id: null });
      jest.spyOn(vehicleTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleType }));
      saveSubject.complete();

      // THEN
      expect(vehicleTypeFormService.getVehicleType).toHaveBeenCalled();
      expect(vehicleTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleType>>();
      const vehicleType = { id: 123 };
      jest.spyOn(vehicleTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
