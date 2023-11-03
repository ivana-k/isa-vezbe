import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessMessageComponent } from './success-message.component';

describe('SuccessMessageComponent', () => {
  let component: SuccessMessageComponent;
  let fixture: ComponentFixture<SuccessMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuccessMessageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuccessMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
