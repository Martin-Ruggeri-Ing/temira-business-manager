import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VehicleBrandService } from '../service/vehicle-brand.service';
import { IVehicleBrand } from '../vehicle-brand.model';
import { VehicleBrandFormService } from './vehicle-brand-form.service';

import { VehicleBrandUpdateComponent } from './vehicle-brand-update.component';

describe('VehicleBrand Management Update Component', () => {
  let comp: VehicleBrandUpdateComponent;
  let fixture: ComponentFixture<VehicleBrandUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleBrandFormService: VehicleBrandFormService;
  let vehicleBrandService: VehicleBrandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VehicleBrandUpdateComponent],
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
      .overrideTemplate(VehicleBrandUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleBrandUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleBrandFormService = TestBed.inject(VehicleBrandFormService);
    vehicleBrandService = TestBed.inject(VehicleBrandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vehicleBrand: IVehicleBrand = { id: 456 };

      activatedRoute.data = of({ vehicleBrand });
      comp.ngOnInit();

      expect(comp.vehicleBrand).toEqual(vehicleBrand);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleBrand>>();
      const vehicleBrand = { id: 123 };
      jest.spyOn(vehicleBrandFormService, 'getVehicleBrand').mockReturnValue(vehicleBrand);
      jest.spyOn(vehicleBrandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleBrand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleBrand }));
      saveSubject.complete();

      // THEN
      expect(vehicleBrandFormService.getVehicleBrand).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleBrandService.update).toHaveBeenCalledWith(expect.objectContaining(vehicleBrand));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleBrand>>();
      const vehicleBrand = { id: 123 };
      jest.spyOn(vehicleBrandFormService, 'getVehicleBrand').mockReturnValue({ id: null });
      jest.spyOn(vehicleBrandService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleBrand: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleBrand }));
      saveSubject.complete();

      // THEN
      expect(vehicleBrandFormService.getVehicleBrand).toHaveBeenCalled();
      expect(vehicleBrandService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleBrand>>();
      const vehicleBrand = { id: 123 };
      jest.spyOn(vehicleBrandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleBrand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleBrandService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
