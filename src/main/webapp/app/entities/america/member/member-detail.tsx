import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './member.reducer';

export const MemberDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const memberEntity = useAppSelector(state => state.member.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memberDetailsHeading">Member</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{memberEntity.id}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{memberEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{memberEntity.lastName}</dd>
          <dt>
            <span id="birthDate">Birth Date</span>
          </dt>
          <dd>
            {memberEntity.birthDate ? <TextFormat value={memberEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="gender">Gender</span>
          </dt>
          <dd>{memberEntity.gender}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{memberEntity.address}</dd>
          <dt>
            <span id="city">City</span>
          </dt>
          <dd>{memberEntity.city}</dd>
          <dt>
            <span id="state">State</span>
          </dt>
          <dd>{memberEntity.state}</dd>
          <dt>
            <span id="zip">Zip</span>
          </dt>
          <dd>{memberEntity.zip}</dd>
          <dt>
            <span id="county">County</span>
          </dt>
          <dd>{memberEntity.county}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{memberEntity.country}</dd>
          <dt>
            <span id="tobaccoUseIndicator">Tobacco Use Indicator</span>
          </dt>
          <dd>{memberEntity.tobaccoUseIndicator ? 'true' : 'false'}</dd>
          <dt>
            <span id="substanceAbuseIndicator">Substance Abuse Indicator</span>
          </dt>
          <dd>{memberEntity.substanceAbuseIndicator ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdateDatetime">Last Update Datetime</span>
          </dt>
          <dd>
            {memberEntity.lastUpdateDatetime ? (
              <TextFormat value={memberEntity.lastUpdateDatetime} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/member" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/member/${memberEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MemberDetail;
